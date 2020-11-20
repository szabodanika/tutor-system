<template>
    <b-row>
      <b-col xl='6' md='6'>
        <b-card class='container upcoming' no-body v-if='lessons'>
          <template #header>
            <b-row align-v='center'>
              <b-col align='left'>
                <h2 class='text-primary'> Upcoming Lessons </h2>
              </b-col>
            </b-row>
          </template>
        <LargeLessonPreview :lesson='lesson' v-for='(lesson, index) in upcomingLessons'
                            v-bind:key='index'></LargeLessonPreview>
        </b-card>
      </b-col>
      <b-col xl='6' md='6'>
        <b-card class='container' no-body v-if='lessons'>
          <template #header>
            <b-row align-v='center'>
              <b-col align='left'>
                <h2 class='text-primary'> Past Lessons </h2>
              </b-col>
            </b-row>
          </template>
        <LargeLessonPreview :lesson='lesson' v-for='(lesson, index) in pastLessons'
                            v-bind:key='index'></LargeLessonPreview>
        </b-card>
      </b-col>
    </b-row>
</template>

<script>
import LargeLessonPreview from "@/components/LargeLessonPreview";
import StudentPreview from "@/components/MiniStudentPreview";

export default {
  name: "LessonList",
  components: {StudentPreview, LargeLessonPreview},
  props: [
    'user'
  ],
  watch: {},
  data() {
    return {
      lessons: null,
      upcomingLessons: [],
      pastLessons: [],
      days: [],
      isLoading: true,
    }
  },
  mounted() {
    this.getLessons()
  },
  methods: {
    getLessons() {
      this.isLoading = true
      this.$services.service.getLessonByStudent(this.user.id).then((res) => {
        this.lessons = res
        this.lessons.forEach((lesson) => {
          if (lesson.start > new Date()) {
            this.upcomingLessons.push(lesson)
          } else {
            this.pastLessons.push(lesson)
          }
        })
        this.isLoading = false
      }).catch((error) => {
        // check if we have a string response data. these are usually custom defined on server side
        if (error.response && (typeof error.response.data === "string" || error.response.data instanceof String))
          this.$nuxt.$emit("error", error.response.data)
        // check if we have an error message
        else if (typeof error.message === "string" || error.message instanceof String) this.$nuxt.$emit("error", error.message)
        // no error message, this was unexpected
        else this.$nuxt.$emit("error", "unexpected_error")
      })
    },
    getWeekNumber(date) {
      let dayn = (date.getDay() + 6) % 7;
      date.setDate(date.getDate() - dayn + 1);
      let firstThursday = date.valueOf();
      date.setMonth(0, 1);
      if (date.getDay() !== 4) {
        date.setMonth(0, 1 + ((4 - date.getDay()) + 7) % 7);
      }
      return 1 + Math.ceil((firstThursday - date) / 604800000);
    },
    getMondayOfWeek(w, y) {
      if (!y) y = new Date().getFullYear()
      let firstDayOfYear = new Date(y, 0, 0).getDay()
      var monday = (w - 1) * 7 - firstDayOfYear + 1;
      console.log(monday)
      console.log(`day ${monday} of year ${y} is ${new Date(y, 0, monday)}`)
      return new Date(y, 0, monday);
    },
    formatDate(date) {
      if (!date) return "error"
      return date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate()
    },
    formatDateWithDay(date) {
      if (!date) return "error"
      let append = null
      let dateLastDigit = date.getDate().toString().substr(date.getDate().toString().length - 1)
      if (dateLastDigit == 1) {
        append = "st"
      } else if (dateLastDigit == 2) {
        append = "nd"
      } else if (dateLastDigit == 3) {
        append = "rd"
      } else append = "th"
      if (date.getDate() == 11 || date.getDate() == 12 || date.getDate() == 13) {
        append = "th"
      }
      return this.daysOfWeek[date.getDay() == 0 ? 6 : date.getDay() - 1] + " " + date.getDate() + append
    },
    formatDateTime(date) {
      if (!date) return "error"
      return date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " " +
          date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds()
    }
  },
}
</script>

<style scoped>
.container {
  padding-bottom: 0.5rem;
}

.upcoming {
  margin-bottom: 3rem;
}

.noLessons {
  margin: 1rem;
}

.table {
  padding-bottom: 0.5rem;
  margin-bottom: 0;
}

</style>