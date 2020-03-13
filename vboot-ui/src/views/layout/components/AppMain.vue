<template>
  <section class="app-main">
    <div class="router">
      <transition name="fade-transform" mode="out-in">
        <!-- or name="fade" -->
        <!-- <router-view :key="key"></router-view> -->
        <router-view/>
      </transition>
    </div>
  </section>
</template>

<script>
export default {
  name: "AppMain",
  computed: {
    // key() {
    //   return this.$route.name !== undefined ? this.$route.name + +new Date() : this.$route + +new Date()
    // }
  }
};
</script>

<style lang="scss" scoped>
/* .app-main {
  min-height: calc(100vh - 50px);
  position: relative;
  overflow: hidden;
} */
.app-main {
  position: absolute;
  height: 100%;
  width: 100%;
  background: #f1f4f5;
  padding: 15px;
  box-sizing: border-box;
  overflow-y: scroll;
  .router {
    height: 100%;
    padding: 15px 8px;
    background: #fff;
    min-height: 100%;
    height: auto;
    overflow: hidden;
    border-radius: 5px;
    box-sizing: border-box;
  }
}
</style>
