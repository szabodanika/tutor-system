<template>
  <div class="container" v-if='user'>
    <h2>
      Welcome back, {{user.firstName}}
    </h2>
    <br>
    <b-row>
      <b-col>
        <WeekView :tutor='user.id' :user='user' :week='week'></WeekView>
      </b-col>
    </b-row>
  </div>
  <div class="container" v-else>
    <h2>
      Welcome back, {{user.firstName}}
    </h2>
    <br>
    <b-row>
      <b-col>
        <WeekView :tutor='user.id' :user='user' :week='week'></WeekView>
      </b-col>
    </b-row>
  </div>
</template>

<script>

import WeekView from "@/components/WeekView";

export default {
  components: {WeekView},
  layout: 'index',
  name: 'Index',
  props: [
    'user'
  ],
  data() {
    return {
      week: this.getWeekNumber(new Date())
    }
  },
  created() {
    if (!this.user)  {
      this.$nuxt.$emit("error", "not_logged_in")
      return
    }
  },
  methods: {
    getWeekNumber(date) {
      let dayn = (date.getDay() + 6) % 7;
      date.setDate(date.getDate() - dayn + 3);
      let firstThursday = date.valueOf();
      date.setMonth(0, 1);
      if (date.getDay() !== 4) {
        date.setMonth(0, 1 + ((4 - date.getDay()) + 7) % 7);
      }
      return 1 + Math.ceil((firstThursday - date) / 604800000);
    },
  }
}
</script>

<style>
</style>
