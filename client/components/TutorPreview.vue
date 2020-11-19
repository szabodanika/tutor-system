<template>
  <div>
    <b-card id='container' v-if='tutor'>
      <template #header>
        <b-row no-gutters align-v='center'>
          <b-col align='right'>
            <div class="d-flex align-items-center">
              <b-avatar
                  :text="initials"
                  variant="primary"></b-avatar>
              <span style='margin-left: 1rem'>{{ tutor.firstName }} {{ tutor.lastName }}</span>
            </div>
          </b-col>
          <b-col align='right'>
            <h2 class='text-primary'> Your Tutor </h2>
          </b-col>
        </b-row>
      </template>
      <b-col no-gutters>
        <b-row>
          <b-col cols='5'>
            <b-row>
              Email
            </b-row>
            <b-row>
              Phone
            </b-row>
            <b-row>
              Tutor Code
            </b-row>
          </b-col>
          <b-col>
            <b-row>
              {{ tutor.email?tutor.email:"Not added yet"  }}
            </b-row>
            <b-row>
              {{ tutor.phone?tutor.phone:"Not added yet" }}
            </b-row>
            <b-row>
              {{ tutor.tutorCode }}
            </b-row>
          </b-col>
        </b-row>
      </b-col>
      <div v-if='tutor.info'>
        <br>
        <h4>
          Info
        </h4>
        {{tutor.info}}
        <br>
        <br>
      </div>
    </b-card>
  </div>
</template>

<script>
export default {
  name: "TutorPreview",
  props: [
    'tutor',
    'user'
  ],
  data() {
    return {
      initials: ""
    }
  },
  created() {
    if(this.tutor.firstName) this.initials += this.tutor.firstName.substr(0,1)
    if(this.tutor.lastName) this.initials += this.tutor.lastName.substr(0,1)
    // this.getStudent()
  },
  methods: {
    getStudent() {
      this.$services.service.getUserById(this.tutor).then((res) => {
        this.tutor = res

        this.tutor.totalHours = 0
        this.tutor.studentLessons.forEach((lesson) => {
          this.tutor.totalHours += lesson.hours
        })

        if (this.tutor.firstName) this.initials += this.tutor.firstName.substr(0, 1)
        if (this.tutor.lastName) this.initials += this.tutor.lastName.substr(0, 1)

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
    },
    formatDate(date) {
      return date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate()
    },
    formatDateTime(date) {
      return date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " " +
          date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds()
    },
    editStudent() {
      this.$nuxt.$router.push(`student/${this.tutor.id}`)
    }
  }
}
</script>

<style scoped>
#container {
  /*padding: 0.5rem;*/
  /*min-width: 200px;*/
  font-weight: 400;
  /*max-height: 20rem;*/
}
</style>