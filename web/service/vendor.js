module.exports = {
  gengerateRandomId(prefix) {
    return prefix + ((new Date()).getTime().toString().substring(9, 3)) + parseInt(Math.random() * 10000, 10);
  },
  setFirstLetterToUppercase(str) {
    return str.substr(0, 1).toUpperCase() + str.substring(1);
  }
};
