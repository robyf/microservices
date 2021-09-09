import React from 'react';
import { connect } from 'react-redux';

import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Card from 'react-bootstrap/Card';
import Button from 'react-bootstrap/Button';
import { LinkContainer } from 'react-router-bootstrap';

import { Account, CreditDecision } from '../../../../types';

const Dashboard = ({ account, creditDecision }) => {
  return (
    <>
      <Row className="scoring-header">
        <Col>
          <Card className="scoring-card">
            <Card.Body>
              <Row>
                <Col sm={6} className="text-right">
                  Maximum credit amount:
                </Col>
                <Col sm={6}>
                  {creditDecision.amount}
                </Col>
              </Row>
              <Row>
                <Col sm={6} className="text-right">
                  Current available balance:
                </Col>
                <Col sm={6}>
                  {account.balance}
                </Col>
              </Row>
            </Card.Body>
          </Card>
        </Col>
      </Row>
      <Row className="scoring-actions">
        <Col className="text-right">
          <LinkContainer to="/withdraw">
            <Button variant="primary" disabled={account.balance <= 0}>
              Withdraw
            </Button>
          </LinkContainer>
        </Col>
        <Col>
          <LinkContainer to="/deposit">
            <Button variant="primary" disabled={account.balance >= creditDecision.amount}>
              Pay back
            </Button>
          </LinkContainer>
        </Col>
      </Row>
    </>
  );
};

Dashboard.propTypes = {
  account: Account.propTypes,
  creditDecision: CreditDecision.propTypes,
};

const mapStateToProps = state => {
  return {
    account: state.session.account,
    creditDecision: state.session.creditDecision,
  };
};

export default connect(mapStateToProps)(Dashboard);
