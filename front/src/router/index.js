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
                path: 'food',
                name: 'Food',
                component: () => import("../views/Food"),
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
                path: 'order',
                name: 'Order',
                component: () => import("../views/Order")
            }
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
