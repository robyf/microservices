import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import moment from 'moment';
import { withRouter } from 'react-router-dom';

import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Table from 'react-bootstrap/Table';

import { Account } from '../../types';
import { accountEvents } from '../../api/account';
import { UnauthorizedError } from '../../api/common';
import { logout as logoutAction } from '../../redux/actions';

const Events = ({ account, logoutAction, history }) => {

  const [loading, setLoading] = React.useState(true);
  const [events, setEvents] = React.useState([]);

  React.useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      if (account) {
        try {
          const evts = await accountEvents();
          console.log("Events", evts);
          setEvents(evts);
          setLoading(false);
        } catch (err) {
          if (err instanceof UnauthorizedError) {
            console.error("Unauthorized, should redirect to login page");
            logoutAction();

            const { push } = history;
            push('/login');
          } else {
            console.error("Received error", err);
          }
        }
      }
    };
    fetchData();
  }, [account]);

  const eventType = type => {
    switch(type) {
      case 1000:
        return "Credit decision accepted";
      case 1001:
        return "Deposit";
      case 2000:
        return "Withdraw";
      default:
        return `Unknown event ${type}`;
    }
  };

  const formatDate = date => {
    return moment(date).format("D.M.YYYY H:mm");
  };

  if (loading) {
    return "Loading...";
  }

  return (
    <>
      <Row className="scoring-header">
        <Col className="text-center">
          Account events
        </Col>
      </Row>
      <Table striped bordered hover className="scoring-card">
        <thead>
          <tr>
            <th>Time</th>
            <th>Type</th>
            <th>Amount</th>
            <th>Resulting balance</th>
          </tr>
        </thead>
        <tbody>
          {events.map((event, idx) => (
            <tr key={idx}>
              <td>{formatDate(event.time)}</td>
              <td>{eventType(event.type)}</td>
              <td>{event.amount}</td>
              <td>{event.resultingBalance}</td>
            </tr>
          ))}
        </tbody>
      </Table>
    </>
  );
};

Events.propTypes = {
  account: Account.propTypes,
  logoutAction: PropTypes.func.isRequired,
  history: PropTypes.shape({
    push: PropTypes.func.isRequired,
  }).isRequired,
};

const mapStateToProps = state => {
  return {
    account: state.session.account,
  };
};

export default connect(mapStateToProps, { logoutAction })(withRouter(Events));
