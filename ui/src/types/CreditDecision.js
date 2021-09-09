import PropTypes from 'prop-types';

class CreditDecision {
  constructor(json) {
    this.id = json.id;
    this.status = json.status;
    this.positive = json.positive;
    this.amount = json.amount;
  }
}

CreditDecision.propTypes = PropTypes.shape({
  id: PropTypes.string.isRequired,
  status: PropTypes.string.isRequired,
  positive: PropTypes.bool.isRequired,
  amount: PropTypes.number.isRequired,
});

export default CreditDecision;
