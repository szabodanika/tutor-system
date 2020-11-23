import Service from '~/services/service'
require('dotenv').config()

export default ({ app }, inject) => {
  let baseUrl = process.env.API_BASE_URL

  app.$axios.defaults.withCredentials = true
	const services = {
		service: new Service(app.$axios, baseUrl),
	}

	inject('services', services)
}
