<template>
  <b-overlay :show="isLoading" :rounded='true' variant='transparent'
             spinner-type='grow'  :opacity="1.0"  blur="1rem">
    <b-card id='container' no-body>
      <template #header>
        <b-row align-v='center'>
          <b-col align='left'>
            <h2  class='text-primary'> Week {{ week }} </h2>
          </b-col>
          <b-col align='right'>
            <h5>
              {{ formatDate(date) }}
            </h5>
          </b-col>
        </b-row>
      </template>
      <span>
<!--          style='overflow-x: scroll'-->
        <b-table responsive='xl' class='table' v-if='fields && fields.length > 1' hover :fields='fields'
                 :items='days'
                 ref='week-table'
                 small >
          <template #cell(day)="data" align-v='center'>
             {{ formatDateWithDay(days[data.index].day) }}
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
            <StudentPreview :student='key' style='margin-bottom: 0;'>
            </StudentPreview>
          </template>
        </b-table>
        <p v-if='!lessons || lessons.length == 0' class='noLessons'>
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
    },
    lessons: {
      handler: function (lessons) {

        this.fields = [{key: 'day', label: ''}]
        this.days = []

        for (let i = 0; i < 7; i++) {
          let date = this.getMondayOfWeek(this.week, this.year)
          let date2 = new Date(date.getFullYear(), date.getMonth(), date.getDate() + i)
          this.days.push({day: date2})
        }
        this.students = this.user.students
        this.students.forEach((student) => {
          this.studentsKeys.push(student.id)
          this.fields.push({
            key: student.id.toString(),
            label: student.firstName + " " + student.lastName
          })
        })

        lessons.forEach((lesson) => {
          this.days[lesson.start.getDay() == 0 ? 6 : lesson.start.getDay() - 1][lesson.student.id] = lesson
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
      days: [],
      students: [],
      studentsKeys: [],
      isLoading: true,
      year: 2020,
      date: this.getMondayOfWeek(this.week, this.year),
      daysOfWeek: [
        "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
      ]
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
      date.setDate(date.getDate() - dayn + 1);
      let firstThursday = date.valueOf();
      date.setMonth(0, 1);
      if (date.getDay() !== 4) {
        date.setMonth(0, 1 + ((4 - date.getDay()) + 7) % 7);
      }
      return 1 + Math.ceil((firstThursday - date) / 604800000);
    },
    getMondayOfWeek(w, y) {
      if(!y) y = new Date().getFullYear()
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
      if(date.getDate() == 11 || date.getDate() == 12 || date.getDate() == 13){
        append = "th"
      }
      return  this.daysOfWeek[date.getDay() == 0 ? 6 : date.getDay() - 1] + " " + date.getDate() + append
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
#container {
  margin-bottom: 1rem;
}

.noLessons {
  margin: 1rem;
}

.table {
  padding-bottom: 0.5rem;
  margin-bottom: 0;
}

</style>