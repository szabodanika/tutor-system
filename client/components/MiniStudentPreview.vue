<template>
  <b-overlay :show="isLoading" rounded="sm" style='max-width: 14rem' :rounded='true' variant='transparent'
             spinner-type='grow' :opacity="1.0" blur="1rem">
    <b-card no-body id='container' style='padding-top: 0'>
      <b-col v-if='studentData'>
        <b-row no-gutters align-v='center'>
          <b-col align='left' cols='4'>
            <b-avatar
                :text="initials"
                variant="primary"></b-avatar>
          </b-col>
          <b-col align='right' cols='8'>
            <span style='margin-left: 1rem'>{{ studentData.firstName }}</span>
          </b-col>
          <br>
          <br>
          <br>
          <b-col style='padding-left: 1rem'>
            <b-row>
              £{{ studentData.totalDebt }} unpaid
            </b-row>
            <b-row>
              {{ studentData.totalHours }} Total hours
            </b-row>
          </b-col>
          <b-col align='center' style='margin-top: 0.5rem'>
            <b-button-group>
              <b-button  @click='$nuxt.$router.push(`newlesson/${studentData.id}`)' variant='outline-primary'
                         size='sm'>
                <b-icon icon='plus'></b-icon>
              </b-button>
              <b-button  @click='$nuxt.$router.push(`student/${studentData.id}`)' variant='outline-primary'
                         size='sm'>
                <b-icon icon='pencil'></b-icon>
              </b-button>
              <b-button  @click='hide' variant='outline-primary'
                         size='sm'>
                <b-icon icon='eye-slash'></b-icon>
              </b-button>
            </b-button-group>
          </b-col>
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
      hidden: false
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
    },
    hide() {
      this.$nuxt.$emit("hide", this.studentData)
    }
  }
}
</script>

<style scoped>
#container {
  width: 10rem;
  padding: 0.5rem;
  font-weight: 400;
  height: 11rem;
}
</style>