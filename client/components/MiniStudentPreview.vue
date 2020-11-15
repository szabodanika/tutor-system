<template>
  <b-overlay :show="isLoading" rounded="sm" style='max-width: 14rem' :rounded='true' variant='transparent'
             spinner-type='grow'  :opacity="1.0"  blur="1rem">
    <b-card no-body id='container' style='padding-top: 0'>
      <b-col v-if='studentData' >
        <b-row no-gutters align-v='center' >
          <b-col align='left' cols='2'>
            <b-avatar
                :text="initials"
                variant="primary"></b-avatar>
          </b-col>
          <b-col align='left'  cols='10'>
            <span style='margin-left: 1rem'>{{ studentData.firstName }} {{ studentData.lastName }}</span>
          </b-col>
          <br>
          <br>
          <br>
          <b-col cols='6' style='padding-left: 1rem; padding-right: 1rem'>
            <b-row>
              Unpaid
            </b-row>
            <b-row>
              Rate
            </b-row>
            <b-row>
              Hours
            </b-row>
          </b-col>
          <b-col cols='6'>
            <b-row>
              £{{ studentData.totalDebt }}
            </b-row>
            <b-row>
              £{{ studentData.rate }}
            </b-row>
            <b-row>
              {{ studentData.totalHours }}
            </b-row>
          </b-col>
        </b-row>
        <b-row no-gutters align-v='center'>

        </b-row>
      </b-col>

    </b-card>
  </b-overlay>
</template>

<script>
export default {
  name: "MiniStudentPreview",
  props: [
    'student'
  ],
  data() {
    return {
      studentData: null,
      initials: "",
      isLoading: true,
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

        if (this.studentData.firstName) this.initials += this.studentData.firstName.substr(0, 1)
        if (this.studentData.lastName) this.initials += this.studentData.lastName.substr(0, 1)

        this.$emit("loaded")
        this.isLoading = false
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
  width: 14rem;
  padding: 0.5rem;
  font-weight: 400;
  height: 10rem;
}
</style>