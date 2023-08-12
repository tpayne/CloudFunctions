/*
 Example module for NodeJS
 */
const axios = require('axios')

function getToken (req) {
  if (req.headers.authorization && req.headers.authorization.split(' ')[0] === 'Bearer') {
    return req.headers.authorization.split(' ')[1]
  } else if (req.query && req.query.token) {
    return req.query.token
  }
  return null
}

module.exports = async function (context, req) {
  try {
    context.log('JavaScript HTTP trigger function processed a request.')

    const userName = (req.query.username || (req.body && req.body.username))
    const authToken = getToken(req)

    // fail if incoming data is required
    if (!userName) {
      context.res = {
        status: 400,
        body: { message: 'Error: User name is missing' },
        contentType: 'application/json'
      }
      return
    }

    let gitHubUrl = 'https://api.github.com/users/' + userName + '/repos'
    if (!authToken) {
      gitHubUrl += '?visibility=public'
    } else {
      gitHubUrl += '?visibility=private'
    }

    let config = {}
    if (authToken) {
      config = {
        Accept: 'application/json',
        Authorization: `Bearer ${authToken}`
      }
    } else {
      config = {
        Accept: 'application/json'
      }
    }

    context.log('Running %s', gitHubUrl)
    await axios.get(gitHubUrl, { headers: config })
      .then(response => {
        context.res = {
          status: 200,
          body: response.data,
          contentType: 'application/json'
        }
      })
      .catch(error => {
        throw (error)
      })
  } catch (err) {
    context.res = {
      status: 500,
      body: { message: err.message },
      contentType: 'application/json'
    }
  }
}
