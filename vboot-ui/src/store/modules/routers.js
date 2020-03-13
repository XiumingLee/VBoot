import {constantRouterMap} from '@/router/index'

const routers = {
    state: {
      data:null,
    },
  
    mutations: {
        SET_routes: (state, routes) => {
            if (routers === null) {
                state.data = null;
            } else {
                state.data = constantRouterMap.concat(routes);
            }
          },
    }
}

export default routers