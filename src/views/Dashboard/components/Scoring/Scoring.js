import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';

import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Form from 'react-bootstrap/Form';
import Card from 'react-bootstrap/Card';
import Button from 'react-bootstrap/Button';

import { Account } from '../../../../types';
import { createCreditDecision } from '../../../../api/account';
import { setCreditDecision } from '../../../../redux/actions';

const Scoring = ({ account, setCreditDecision }) => {

  const [income, setIncome] = React.useState('');
  const [disabled, setDisabled] = React.useState(true);
  const [loading, setLoading] = React.useState(false);

  const incomeChange = value => {
    setIncome(value);

    const n = parseFloat(value);
    setDisabled(isNaN(n) || !isFinite(n) || n < 0);
  };

  const buttonClicked = async () => {
    console.log('Button clicked');
    setLoading(true);

    const cd = await createCreditDecision(parseFloat(income));
    console.log('Credit decision', cd);
    setCreditDecision(cd);
  }

  return (
    <>
      <Row className="scoring-header">
        <Col className="text-center">
          Welcome to Awesome Credit. Please answer some simple questions to receive a loan offer from us!
        </Col>
      </Row>
      <Row>
        <Col>
          <Card className="scoring-card">
            <Card.Body>
              <Form>
                <Form.Group as={Row} controlId="formIncome">
                  <Form.Label column sm={4}>
                    Monthly net income
                  </Form.Label>
                  <Col sm={8}>
                    <Form.Control type="number" min="0" max="10000" step="100" placeholder="Net income" value={income} onChange={event => incomeChange(event.target.value)} />
                  </Col>
                </Form.Group>
                <Form.Group as={Row}>
                  <Col sm={{ span: 8, offset: 4 }}>
                    <Button variant="primary" disabled={disabled || loading} onClick={() => buttonClicked()}>
                      {loading ? 'Scoring...' : 'Continue'}
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

Scoring.propTypes = {
  account: Account.propTypes,
  setCreditDecision: PropTypes.func.isRequired,
};

const mapStateToProps = state => {
  return {
    account: state.session.account,
  };
};

export default connect(mapStateToProps, { setCreditDecision })(Scoring);
