import PropTypes from 'prop-types';

class Event {
  constructor(json, balance) {
    this.id = json.id;
    this.time = new Date(Date.parse(json.time));
    this.type = json.type;
    this.amount = json.amount;
    this.resultingBalance = json.resultingBalance;
  }
}

Event.propTypes = PropTypes.shape({
  id: PropTypes.string.isRequired,
  time: PropTypes.instanceOf(Date).isRequired,
  type: PropTypes.number.isRequired,
  amount: PropTypes.number.isRequired,
  resultingBalance: PropTypes.number.isRequired,
});

export default Event;
