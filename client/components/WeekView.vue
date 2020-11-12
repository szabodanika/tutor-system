<template>
  <b-overlay :show="isLoading" rounded="sm">
    <b-card id='container' no-body>
      <template #header>
        <b-row>
          <b-col align='left'>
            <h2> Week {{ week }} </h2>
          </b-col>
          <b-col align='right'>
            <h5>
              {{ formatDate(date) }}
            </h5>
          </b-col>
        </b-row>
      </template>
      <span style='overflow-x: scroll'>
        <b-table v-if='fields.length > 1' striped :fields='fields' :items='days' ref='week-table' small>
          <template #cell(day)="data">
            {{ days[data.index].day }}
          </template>
          <template #cell(gex)="data">
            {{ data.item }}
          </template>

          <template v-slot:[`cell(${index})`]="data" v-for='index in studentsKeys'>
            <LessonPreview v-if='data.item[index] != null'
                           :lesson="data.item[index]" style='margin-bottom: 0'>
            </LessonPreview>
          </template>

          <template v-slot:[`head(${key})`]="data" v-for='(key, index) in studentsKeys'>
            <StudentPreview :student='key'>
            </StudentPreview>
          </template>
        </b-table>
        <p class='noLessons'>
           No lessons this week
        </p>
      </span>
    </b-card>
  </b-overlay>
</template>

<script>
import LessonPreview from "@/components/LessonPreview";
import StudentPreview from "@/components/MiniStudentPreview";

export default {
  name: "WeekView",
  components: {StudentPreview, LessonPreview},
  props: [
    'tutor',
    'week',
    'user'
  ],
  watch: {
    week: {
      handler: function (lessons) {
        this.getLessons()
      }
    }
    ,
    lessons: {
      handler: function (lessons) {

        this.fields = [{key: 'day', label: ''}]
        this.days = [
          {day: "Monday",},
          {day: "Tuesday"},
          {day: "Wednesday"},
          {day: "Thursday",},
          {day: "Friday"},
          {day: "Saturday"},
          {day: "Sunday"},
        ]
        this.students = []

        lessons.forEach((lesson) => {
          // check if student is in the list
          let studentAdded = false
          this.students.forEach((student) => {
            if (student.id == lesson.student.id) {
              studentAdded = true
            }
          })
          if (!studentAdded) {
            this.students.push(lesson.student)
            this.studentsKeys.push(lesson.student.id)
            this.fields.push({
              key: lesson.student.id.toString(),
              label: lesson.student.firstName + " " + lesson.student.lastName
            })
          }
          this.days[lesson.start.getDay()][lesson.student.id] = lesson
        })

        this.isLoading = false
        this.$emit("loaded")
      }
    }
  },
  data() {
    return {
      lessons: null,
      fields: [{key: 'day', label: ''}],
      days: [
        {day: "Monday",},
        {day: "Tuesday"},
        {day: "Wednesday"},
        {day: "Thursday",},
        {day: "Friday"},
        {day: "Saturday"},
        {day: "Sunday"},
      ],
      students: [],
      studentsKeys: [],
      isLoading: true,
      date: this.getDateOfWeek(this.week, new Date().getFullYear())
    }
  },
  mounted() {
    if (process.browser)
      this.getLessons()
  },
  methods: {
    getLessons() {
      this.isLoading = true
      this.$services.service.getLessonByTutor(this.user.id, this.week).then((res) => {
        this.lessons = res
      }).catch((error) => {
        if (!error.response) {
          this.$nuxt.$emit("error", "unexpected_error")
        } else {
          this.$nuxt.$emit("error", error.response.data)
        }
      })
    },
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
    getDateOfWeek(w, y) {
      var d = (1 + (w - 1) * 7);
      return new Date(y, 0, d);
    },
    formatDate(date) {
      return date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate()
    },
    formatDateTime(date) {
      return date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " " +
          date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds()
    }
  },
}
</script>

<style scoped>
#container {
  margin-bottom: 1rem;
}

.noLessons {
  margin: 1rem;
}
</style>