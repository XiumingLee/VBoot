import { get } from './request'
import store from '@/store'

/**
 * 判断当前用户是否的登录
 */
export function isLogin() {
  return new Promise((resolve, reject) => {
    get("/isLogin").then(res => {
      if (res.status === 200) {
        store.commit("SET_INFO",res.data)
        resolve()
      } else {
        reject()
      }
    }).catch(err => {
      reject();
    })
  })
}
/**获取当前用户的所有可访问的url */
export function getUserUrls(){
  get("/system/userUrls").then(res=>{
    store.commit("SET_URLS",res.data)
  }).catch(err=>{
    reject(err);
  })
}
/**判断当前用户是否有权发送请求 */
export function hasPermission(url,method){
  const urls = store.state.user.urls;
  let is_true = false;
  for (let index = 0; index < urls.length; index++) {
    const tar = urls[index];
    if ((tar.url === url) && (tar.method === method)) {
      is_true = true;
      break;
    }
  }
  return is_true;
}
/**
 * 获取用户菜单消息
 */
export function getMenu(store,router) {
  return new Promise((resolve, reject) => {
    get("/system/menu").then(res => {
      if (res.status === 200) {
        let fmtRoutes = formatRoutes(JSON.parse(res.data));
        router.addRoutes(fmtRoutes);
        store.commit("SET_routes",fmtRoutes);
        resolve(1)
      } else {
        reject(0)
      }
    }).catch(err => {
      reject(0)
    })
  })
}
// 格式路由
const formatRoutes = (routes) => {
  let fmRoutes = [];
  routes.forEach(router => {
    let {
      path,
      component,
      name,
      meta,
      iconCls,
      children
    } = router;
    if (children && children instanceof Array) {
      children = formatRoutes(children);
    }
    let fmRouter = {
      path: path,
      component(resolve) {
          require(["@/views" + component + ".vue"], resolve);
      },
      name: name,
      iconCls: iconCls,
      meta: meta,
      children: children
    };    
    fmRoutes.push(fmRouter);
  })
  let errPage=  { path: '*', redirect: '/404', hidden: true }
  fmRoutes.push(errPage);
  return fmRoutes;
}
