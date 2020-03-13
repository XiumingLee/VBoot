// 导航卫士
import router from './index'
import store from '../store'
import NProgress from 'nprogress' // Progress 进度条
import 'nprogress/nprogress.css'// Progress 进度条样式
import { Message } from 'element-ui'
import { isLogin, getMenu, getUserUrls } from '@/utils/auth' // 验权

// const whiteList = ['/login'] // 不重定向白名单
router.beforeEach((to, from, next) => {
    next();
    NProgress.start();
    if ((to.path).startsWith("/login")) {
        next();
        NProgress.done()
    } else {
        //需要认真才能访问的
        if (store.state.user.info === null) {
            //后端请求判断是否已经登录，并重新渲染菜单以及路由
            isLogin().then(() => {
                //判断是否已经渲染路由和菜单
                if (store.state.routers.data === null) {
                    getUserUrls();
                    getMenu(store, router).then(() => {
                        next();
                        NProgress.done();
                    }).catch(() => {
                        Message.error({ message: "请求菜单路由失败" });
                        next(`/login?redirect=${to.path}`)
                        NProgress.done();
                    })
                }
                next();
                NProgress.done();
            }).catch(() => {
                next(`/login?redirect=${to.path}`) // 否则全部重定向到登录页
                NProgress.done()
            })
        } else {
            //判断是否已经渲染路由和菜单
            if (store.state.routers.data === null) {
                getUserUrls();

                getMenu(store, router).then(() => {
                    next();
                    NProgress.done();
                }).catch(() => {
                    Message.error({ message: "请求菜单路由失败" });
                    next(`/login?redirect=${to.path}`)
                    NProgress.done();
                })
            } else {
                next();
                NProgress.done();
            }
        }
    }
});

router.afterEach(() => {
    NProgress.done() // 结束Progress
});
