<template>
  <b-overlay :show='isLoading' v-if='user'>
    <b-row align='middle'>
      <b-col>
        <b-button :disabled='user.students.length == 0' pill @click='$nuxt.$router.push("/newpayment")'>
          <b-icon icon='plus-circle'></b-icon>
          Add
          payment
        </b-button>
      </b-col>
    </b-row>
    <br>
    <br>
    <div class="container">
      <b-row>
        <b-col align='middle' v-if='payments && payments.length == 0'>
          <p>
            You don't have any payments yet
          </p>
        </b-col>

        <b-col xl='4' md='6' sm='12' v-for='(payment, index) in payments'>
          <PaymentPreview id='payment' :payment="payment">
          </PaymentPreview>
        </b-col>
      </b-row>
    </div>
  </b-overlay>
</template>

<script>


import PaymentPreview from "@/components/PaymentPreview";

export default {
  components: {PaymentPreview},
  layout: 'index',
  name: 'Payments',
  props: [
    'user',
  ],
  data() {
    return {
      payments: null,
      isLoading: true
    }
  },
  mounted() {
    if (!this.user) {
      this.$nuxt.$emit("error", "not_logged_in")
      return
    }
    this.getStudents()
  },
  methods: {
    getStudents() {
      this.isLoading = true
      this.$services.service.getPaymentsReceived(this.user.id).then((res) => {
        this.payments = res
        this.isLoading = false
      }).catch((error) => {
        if (!error.response && !error.response.data) {
          this.$nuxt.$emit("error", "unexpected_error")
        } else {
          this.$nuxt.$emit("error", error.response.data)
        }
      })
    },
  }
}
</script>

<style>
.weekListItem {
  margin-bottom: 2rem;
}

#payment {
  margin-bottom: 1rem;
}
</style>
