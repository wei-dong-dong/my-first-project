// 单节点K8s CI/CD流水线，Spring Boot应用专用
pipeline {
    agent any  // 单节点用本地Agent即可，无需K8s动态Agent
    environment {
        // 全局环境变量，替换为自己的实际信息
        HARBOR_IP = "192.168.92.201"        // 单节点K8sIP（Harbor地址）
        HARBOR_PROJECT = "demo"           // Harbor新建的项目名
        IMAGE_NAME = "demo-app"           // 镜像名称
        K8S_NAMESPACE = "default"         // 业务应用部署的K8s命名空间
        GIT_REPO = "https://github.com/wei-dong-dong/my-first-project.git"  // 你的代码仓库地址
        GIT_BRANCH = "main"               // 代码分支
        // 引用Jenkins配置的Harbor凭据（ID为harbor-cred）
        HARBOR_CRED = credentials('harbor-cred')
    }
    stages {
        // 阶段1：拉取代码（Git）
        stage('拉取代码') {
            steps {
                echo "===== 开始拉取${GIT_BRANCH}分支代码 ====="
                git url: "${GIT_REPO}", branch: "${GIT_BRANCH}"
                echo "===== 代码拉取完成 ====="
            }
        }

        // 阶段2：Maven编译打包（Spring Boot）
        stage('编译打包') {
            steps {
                echo "===== 开始Maven编译打包 ====="
                // 单节点Jenkins已挂载Docker，内部集成Maven（也可手动安装，或用Maven容器）
                sh 'mvn clean package -DskipTests'
                // 验证jar包是否生成
                sh 'ls -l target/*.jar'
                echo "===== 编译打包完成，生成jar包 ====="
            }
        }

        // 阶段3：构建Docker镜像并推送到Harbor
        stage('构建并推送镜像') {
            steps {
                echo "===== 开始构建Docker镜像 ====="
                // 镜像标签：HarborIP/项目名/镜像名:Jenkins构建号（唯一，便于回滚）
                IMAGE_TAG = "${HARBOR_IP}/${HARBOR_PROJECT}/${IMAGE_NAME}:${BUILD_NUMBER}"
                // 构建镜像
                sh "docker build -t ${IMAGE_TAG} ."
                echo "===== 镜像构建完成：${IMAGE_TAG} ====="

                echo "===== 开始推送镜像到Harbor ====="
                // 登录Harbor（用凭据中的用户名密码）
                sh "docker login ${HARBOR_IP} -u ${HARBOR_CRED_USR} -p ${HARBOR_CRED_PSW}"
                // 推送镜像
                sh "docker push ${IMAGE_TAG}"
                // 登出Harbor（安全）
                sh "docker logout ${HARBOR_IP}"
                // 删除本地镜像（单节点磁盘有限，清理空间）
                sh "docker rmi ${IMAGE_TAG}"
                echo "===== 镜像推送完成 ====="
            }
        }

        // 阶段4：部署到单节点K8s
        stage('部署到K8s') {
            steps {
                echo "===== 开始部署到K8s ${K8S_NAMESPACE}命名空间 ====="
                // 替换app-deploy.yaml中的${BUILD_NUMBER}为实际构建号
                sh "sed -i 's/\\\${BUILD_NUMBER}/${BUILD_NUMBER}/g' app-deploy.yaml"
                // 部署到K8s（Jenkins已绑定K8s权限，可直接执行kubectl）
                sh "kubectl apply -f app-deploy.yaml -n ${K8S_NAMESPACE}"
                // 验证部署状态（等待滚动更新完成）
                sh "kubectl rollout status deployment/demo-app -n ${K8S_NAMESPACE}"
                // 查看应用Pod状态
                sh "kubectl get pods -n ${K8S_NAMESPACE} -l app=demo-app"
                echo "===== 部署完成！应用访问地址：http://${HARBOR_IP}:30090 ====="
            }
        }
    }
    // 流水线后置操作（成功/失败处理）
    post {
        success {
            echo "✅ 【全流程完成】CI/CD流水线执行成功，应用已部署到单节点K8s！"
            echo "🔗 应用访问地址：http://${HARBOR_IP}:30090"
            echo "📌 镜像地址：${HARBOR_IP}/${HARBOR_PROJECT}/${IMAGE_NAME}:${BUILD_NUMBER}"
        }
        failure {
            echo "❌ 【流水线失败】CI/CD流水线执行失败，请查看控制台日志！"
            // 可选：部署失败时自动回滚到上一个版本
            sh "kubectl rollout undo deployment/demo-app -n ${K8S_NAMESPACE}"
            echo "🔙 已自动回滚到上一个可用版本！"
        }
        always {
            // 无论成功失败，清理工作空间（单节点磁盘有限）
            cleanWs()
        }
    }
}