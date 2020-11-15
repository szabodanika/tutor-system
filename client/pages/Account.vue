<template>
  <div class="container" v-if='user'>
    Hello, {{user.firstName}} {{user.lastName}}
    <b-button @click='signOut'>Sign out</b-button>
  </div>
</template>

<script>

import WeekView from "@/components/WeekView";

export default {
  components: {WeekView},
  layout: 'index',
  name: 'Lessons',
  props: [
    'user',
  ],
  data() {
    return {
      lessons: null,
      week: this.getWeekNumber(new Date()),
      extraWeeksBefore: [],
      extraWeeksAfter: [],
    }
  },
  created() {
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
    signOut() {
      this.$cookies.remove('user')
      this.$services.service.signOut();
      this.$nuxt.$emit("event", {name:"signout"})
      this.$nuxt.$router.push("/login")
    }
  }
}
</script>

<style>
 .weekListItem{
   margin-bottom: 2rem;
 }
</style>
