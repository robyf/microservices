import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { withRouter } from 'react-router-dom';

import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Form from 'react-bootstrap/Form';
import Card from 'react-bootstrap/Card';
import Button from 'react-bootstrap/Button';

import { Account } from '../../types';
import { withdraw } from '../../api/account';
import { setAccountBalance } from '../../redux/actions';

const Withdraw = ({ account, setAccountBalance, history }) => {

  const [amount, setAmount] = React.useState('');
  const [disabled, setDisabled] = React.useState(true);
  const [loading, setLoading] = React.useState(false);

  const amountChange = value => {
    setAmount(value);

    const n = parseFloat(value);
    setDisabled(isNaN(n) || !isFinite(n) || n <= 0 || n > account.balance);
  };

  const buttonClicked = async () => {
    console.log('Button clicked');
    setLoading(true);

    const balance = await withdraw(parseFloat(amount));
    console.log('Resulting balance', balance);
    setAccountBalance(balance);

    const { push } = history;
    push('/dashboard');
  }

  return (
    <>
      <Row className="scoring-header">
        <Col className="text-center">
          Withdraw money to own bank account
        </Col>
      </Row>
      <Row>
        <Col>
          <Card className="scoring-card">
            <Card.Body>
              <Form>
                <Row>
                  <Col sm={4} className="text-right">
                    Available balance:
                  </Col>
                  <Col sm={8}>
                    {account.balance}
                  </Col>
                </Row>
                <Form.Group as={Row} controlId="formIncome">
                  <Form.Label column sm={4} className="text-right">
                    Amount:
                  </Form.Label>
                  <Col sm={8}>
                    <Form.Control type="number" min="0" max={account.balance} step="100" placeholder="Amount" value={amount} onChange={event => amountChange(event.target.value)} />
                  </Col>
                </Form.Group>
                <Form.Group as={Row}>
                  <Col sm={{ span: 8, offset: 4 }}>
                    <Button variant="primary" disabled={disabled || loading} onClick={() => buttonClicked()}>
                      {loading ? 'Withdrawing...' : 'Continue'}
                    </Button>
                  </Col>
                </Form.Group>
              </Form>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </>

  );
};

Withdraw.propTypes = {
  account: Account.propTypes,
  setAccountBalance: PropTypes.func.isRequired,
  history: PropTypes.shape({
    push: PropTypes.func.isRequired,
  }).isRequired,
};

const mapStateToProps = state => {
  return {
    account: state.session.account,
  };
};

export default connect(mapStateToProps, { setAccountBalance })(withRouter(Withdraw));
