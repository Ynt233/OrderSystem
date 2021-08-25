import {createRouter, createWebHistory} from 'vue-router'
import Layout from '../layout/Layout.vue'

const routes = [
    {
        path: '/',
        name: 'Layout',
        component: Layout,
        redirect: "/user",//当访问'/'时跳转到user界面
        children: [
            {
                path: 'user',
                name: 'User',
                component: () => import("../views/UserManage"),
            },
            {
                path: 'mlsw',
                name: 'Food_mlsw',
                component: () => import("../views/Food_mlsw"),
            },
            {
              path: 'rxcs',
              name: 'Food_rxcs',
              component: () => import("../views/Food_rxcs")
            },
            {
                path: 'jxcs',
                name: 'Food_jxcs',
                component: () => import("../views/Food_jxcs")
            },
            {
                path: 'zfm',
                name: 'Food_zfm',
                component: () => import("../views/Food_zfm")
            },
            {
                path: 'zbdp',
                name: 'Food_zbdp',
                component: () => import("../views/Food_zbdp")
            },
            {
                path: 'yl',
                name: 'Food_yl',
                component: () => import("../views/Food_yl")
            },
            {
                path: 'personManage',
                name: 'PersonManage',
                component: () => import("../views/PersonManage"),
            },
            {
                path: 'person',
                name: 'Person',
                component: () => import("../views/Person"),
            },
            {
                path: 'orderDetails',
                name: 'OrderDetails',
                component: () => import("../views/OrderDetails")
            },
            {
                path: 'order',
                name: 'Order',
                component: () => import("../views/Order")
            },
            // {
            //     path: 'account',
            //     name: 'Account',
            //     component: () => import("../views/Account")
            // }
        ]
    },
    {
        path: '/about',
        name: 'about',
        component: () => import("../views/UserManage")
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import("../views/Login")
    },
    {
        path: '/register',
        name: 'Register',
        component: () => import("../views/Register")
    },
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

// 限制某些页面禁止未登录访问
let limitPagePath = ['/about']

router.beforeEach((to, from, next) => {
    if (limitPagePath.includes(to.path)) {
        // 判断sessionStorage是否保存了用户信息
        let userStr = sessionStorage.getItem("user") || "{}"
        let user = JSON.parse(userStr)
        if (!user.id) {
            // 跳转到登录页面
            next({path: "/login"})
        } else {
            next()
        }
    } else {
        next()
    }

})

export default router
