import PropTypes from 'prop-types';

class Account {
  constructor(json, balance) {
    this.id = json.id;
    this.status = json.status;
    this.balance = balance ? balance : json.balance;
  }
}

Account.propTypes = PropTypes.shape({
  id: PropTypes.string.isRequired,
  status: PropTypes.string.isRequired,
  balance: PropTypes.number.isRequired,
});

export default Account;
