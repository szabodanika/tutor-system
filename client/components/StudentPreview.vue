<template>
  <b-card id='container' v-if='studentData'>
    <template #header>
      <b-row no-gutters>
        <b-col align='left'>
          <div class="d-flex align-items-center">
            <b-avatar :text="studentData.firstName.substr(0,1) + studentData.lastName.substr(0,1)"
                      variant="primary"></b-avatar>
            <span style='margin-left: 1rem'>{{ studentData.firstName }} {{ studentData.lastName }}</span>
          </div>
        </b-col>
        <b-col align='right'>
          <b-button variant='outline-primary'>
            <b-icon icon='pencil'></b-icon>
          </b-button>
          <b-button variant='outline-danger'>
            <b-icon icon='x-circle'></b-icon>
          </b-button>
        </b-col>
      </b-row>
    </template>
    <b-col no-gutters>
      <b-row>
        <b-col cols='4'>
          <b-row>
            Phone
          </b-row>
          <b-row>
            Email
          </b-row>
          <b-row>
            Join date
          </b-row>
          <b-row>
            Lessons so far
          </b-row>
          <b-row>
            Hours so far
          </b-row>
          <b-row>
            Current debt
          </b-row>
          <b-row>
            Total paid
          </b-row>
          <b-row>
            Rate
          </b-row>
          <b-row v-if='!studentData.activated'>
            Activation code
          </b-row>
        </b-col>
        <b-col>
          <b-row>
            {{ studentData.phone?studentData.phone:"Not added yet" }}
          </b-row>
          <b-row>
            {{ studentData.email?studentData.email:"Not added yet"  }}
          </b-row>
          <b-row>
            {{ formatDate(studentData.registered) }}
          </b-row>
          <b-row>
            {{ studentData.studentLessons.length }}
          </b-row>
          <b-row>
            {{ studentData.totalHours }} hour{{ studentData.totalHours < 2 ? '' : 's' }}
          </b-row>
          <b-row>
            £{{ studentData.totalDebt }}
          </b-row>
          <b-row>
            £{{ studentData.totalPaid }}
          </b-row>
          <b-row>
            £{{ studentData.rate }} per hour
          </b-row>
          <b-row v-if='!studentData.activated'>
            {{ studentData.activationCode  }}
          </b-row>

        </b-col>
      </b-row>
    </b-col>

  </b-card>
</template>

<script>
export default {
  name: "StudentPreview",
  props: [
      'student',
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
    },
    formatDate(date) {
      return date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate()
    },
    formatDateTime(date) {
      return date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " " +
          date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds()
    }
  }
}
</script>

<style scoped>
#container {
  /*padding: 0.5rem;*/
  min-width: 200px;
  font-weight: 400;
  /*max-height: 20rem;*/
}
</style>