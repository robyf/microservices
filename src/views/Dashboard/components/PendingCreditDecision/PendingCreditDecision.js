import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';

import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Card from 'react-bootstrap/Card';
import Button from 'react-bootstrap/Button';

import { Account, CreditDecision } from '../../../../types';
import { lendingAccount, acceptCreditDecision } from '../../../../api/account';
import { setAccount, setCreditDecision } from '../../../../redux/actions';

const PendingCreditDecision = ({ account, creditDecision, setAccount, setCreditDecision }) => {

  const [loading, setLoading] = React.useState(false);

  const accept = async () => {
    console.log('Accept');
    setLoading(true);

    const cd = await acceptCreditDecision(account.id, creditDecision.id);
    const acc = await lendingAccount();

    setCreditDecision(cd);
    setAccount(acc);
  };

  return (
    <>
      <Row className="scoring-header">
        <Col className="text-center">
          Congratulations! We are pleased to offer you a loan with the following terms:
        </Col>
      </Row>
      <Row>
        <Col>
          <Card className="scoring-card">
            <Card.Body>
              <Row>
                <Col sm={6} className="text-right">
                  Maximum amount:
                </Col>
                <Col sm={6}>
                  {creditDecision.amount}
                </Col>
              </Row>
            </Card.Body>
          </Card>
        </Col>
      </Row>
      <Row className="scoring-actions">
        <Col className="text-center">
          <Button variant="primary" disabled={loading} onClick={() => accept()}>
            {loading ? 'Accepting...' : 'Accept'}
          </Button>
        </Col>
      </Row>
    </>
  );
};

PendingCreditDecision.propTypes = {
  account: Account.propTypes,
  creditDecision: CreditDecision.propTypes,
  setAccount: PropTypes.func.isRequired,
  setCreditDecision: PropTypes.func.isRequired,
};

const mapStateToProps = state => {
  return {
    account: state.session.account,
    creditDecision: state.session.creditDecision,
  };
};

export default connect(mapStateToProps, { setAccount, setCreditDecision })(PendingCreditDecision);
