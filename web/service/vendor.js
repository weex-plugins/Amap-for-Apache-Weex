module.exports = {
  gengerateRandomId (prefix) {
    return prefix + ((new Date()).getTime().toString().substring(9,3)) + parseInt(Math.random() * 10000);
  }
};