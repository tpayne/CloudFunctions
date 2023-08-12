/*
 Example module for NodeJS
 */

module.exports = async function (context, req) {
  try {
    context.log('JavaScript HTTP trigger function processed a request.')

    context.res = {
      status: 500,
      body: { version: '1.0.0' },
      contentType: 'application/json'
    }
  } catch (err) {
    context.res = {
      status: 500,
      body: { message: err.message },
      contentType: 'application/json'
    }
  }
}
