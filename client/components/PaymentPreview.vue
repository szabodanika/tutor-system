<template>
  <b-card id='container' v-if='payment && studentData' no-body>
    <template #header>
      <b-row no-gutters>
        <b-col>
          <div class="d-flex align-items-center">
            <b-avatar
                :text="initials"
                variant="primary"></b-avatar>
            <span style='margin-left: 1rem'>{{ owner.firstName }} {{ owner.lastName }} on
              {{formatDate(payment.date)}}</span>
          </div>
        </b-col>
      </b-row>
    </template>
    <b-card-body>
      <p class='text-muted'>
        <span id='amount'>
        £{{payment.amount}}
      </span>
      covers {{ (payment.amount/owner.rate).toFixed(0)}} hour{{
        (payment.amount/owner.rate)>1?"s":""}}
      </p>
      <p v-if='payment.comment'>
        <br>
        {{payment.comment}}
      </p>
    </b-card-body>
  </b-card>
</template>

<script>
export default {
  name: "PaymentPreview",
  props: {
    payment: {},
    owner: {}
  },
  data() {
    return {
      studentData: null,
      initials: ""
    }
  },
  created() {
    this.getStudent()
  },
  methods: {
    getStudent() {
      this.$services.service.getUserById(this.owner.id).then((res) => {
        this.studentData = res

        if(this.studentData.firstName) this.initials += this.studentData.firstName.substr(0,1)
        if(this.studentData.lastName) this.initials += this.studentData.lastName.substr(0,1)

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
      date = new Date(date)
      return date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate()
    },
    formatDateTime(date) {
      date = new Date(date)
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

#amount {
  font-size: 1.4rem;
  color: green;
}
</style>