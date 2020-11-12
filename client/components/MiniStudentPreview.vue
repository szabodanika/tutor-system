<template>
  <b-card no-body id='container'>
    <b-col no-gutters v-if='studentData'>
      <b-row no-gutters>
        <b-col>
          <div class="d-flex align-items-center">
            <b-avatar :text="studentData.firstName.substr(0,1) + studentData.lastName.substr(0,1)"
                      variant="primary"></b-avatar>
            <span style='margin-left: 1rem'>{{studentData.firstName}} {{studentData.lastName}}</span>
          </div>
        </b-col>
      </b-row>
      <br>
      <b-row>
        £{{ studentData.totalDebt }} debt<br>
        £{{ studentData.rate }} per hour<br>
        {{ studentData.totalHours }} hour{{ studentData.totalHours < 2 ? '' : 's' }}<br>
      </b-row>
    </b-col>

  </b-card>
</template>

<script>
export default {
  name: "MiniStudentPreview",
  props: [
    'student'
  ],
  data() {
    return {
      studentData: null
    }
  },
  created() {
    this.getStudent()
  },
  methods: {
    getStudent() {
      this.$services.service.getUserById(this.student).then((res) => {
        this.studentData = res

        this.studentData.totalHours = 0
        this.studentData.studentLessons.forEach((lesson) => {
          this.studentData.totalHours += lesson.hours
        })

        this.$emit("loaded")
      }).catch((error) => {
        if (!error.response) {
          this.$bvToast.toast("Unexpected error while loading student data", {
            title: 'Error',
            toaster: 'b-toaster-top-center',
            variant: 'danger'
          })
        } else if (error.response.data == "incorrect_credentials") {
          this.$bvToast.toast("Incorrect credentials", {
            title: 'Error',
            toaster: 'b-toaster-top-center',
            variant: 'danger'
          })
        }
      })
    }
  }
}
</script>

<style scoped>
#container {
  padding: 0.5rem;
  min-width: 200px;
  font-weight: 400;
}
</style>