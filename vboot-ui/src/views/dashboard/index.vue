<template>
  <div class="dashboard-container">
    <h1>欢迎来到VBoot系统！</h1>
    <div class="dashboard-text">当前用户:{{ info.name }}</div>
  </div>
</template>

<script>
// import { mapGetters } from 'vuex'

export default {
  name: "Dashboard",
  computed: {
    // ...mapGetters([
    //   'info'
    // ])
    info() {
      return this.$store.state.user.info;
    }
  },
  created() {
    this.$store.commit("SOCKET_RECONNECT");
  },
  methods: {}
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
</style>
