import { createStore } from 'vuex'

export default createStore({
  state: {
    user: {},
    order: {}
  },
  mutations: {
    SET_USER(state, user) {
      state.user = user;
    },
    SET_ORDER(state, order){
      state.order = order;
    }
  },
  actions: {
    SET_USER({commit}, user) {
      this.state.user = user;
    },
    SET_ORDER({commit}, order){
      this.state.order = order;
    }
  },
  getters: {
    getUser: (state) => state.user,
    getOrder: (state) => state.order
  }
})
