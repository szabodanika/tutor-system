<template>
  <div class="container" v-if='user'>
    <b-row align='middle' class='addLessonButton'>
      <b-col>
        <b-button :disabled='user.students.length == 0' pill
                  @click='$nuxt.$router.push(`/newlesson`)'>
          <b-icon icon='plus-circle'></b-icon>
          Add lesson
        </b-button>
      </b-col>
    </b-row>

    <b-button
        @click='extraWeeksBefore.push(week-(extraWeeksBefore.length+1)); extraWeeksBefore = extraWeeksBefore.sort()'>
      <b-icon icon='arrow-left'></b-icon>
      Previous week
    </b-button>
    <br>
    <br>
    <WeekView class='weekListItem' v-if='user' :user='user' @error='handleError' :tutor='user.id' :week='week' v-for='(week, index) in
    extraWeeksBefore'
              v-bind:key='index'/>
    <WeekView class='weekListItem' v-if='user' :user='user' @error='handleError' :tutor='user.id' :week='week'/>
    <WeekView class='weekListItem' v-if='user' :user='user' @error='handleError' :tutor='user.id' :week='week+1'/>
    <WeekView class='weekListItem' v-if='user' :user='user' @error='handleError' :tutor='user.id' :week='week'
              v-for='(week, index) in extraWeeksAfter'
              v-bind:key='index'/>
    <b-button @click='extraWeeksAfter.push(week+extraWeeksAfter.length+2)'>Next week
      <b-icon
          icon='arrow-right'></b-icon>
    </b-button>
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
    handleError(error) {
      this.$nuxt.$emit(error)
    }
  }
}
</script>

<style>
.weekListItem {
  margin-bottom: 2rem;
}

.addLessonButton {
  margin-bottom: 1rem;
}
</style>
