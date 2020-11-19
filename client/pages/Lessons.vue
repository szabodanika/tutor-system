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

    <b-row>
      <b-col align='left'>
        <b-button
            @click='week--'>
          <b-icon icon='arrow-left'></b-icon>
          Previous week
        </b-button>
      </b-col>
      <b-col align='right'>
        <b-button @click='week++'>Next week
          <b-icon
              icon='arrow-right'></b-icon>
        </b-button>
      </b-col>
    </b-row>

    <br>
    <WeekView class='weekListItem' v-if='user' :user='user' @error='handleError' :tutor='user.id' :week='week'/>
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

<style scoped>
.weekListItem {
  margin-bottom: 2rem;
}

.addLessonButton {
  margin-bottom: 1rem;
}
</style>
