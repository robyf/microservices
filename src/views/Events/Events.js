import React from 'react';
import { connect } from 'react-redux';
import moment from 'moment';

import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Table from 'react-bootstrap/Table';

import { Account } from '../../types';
import { accountEvents } from '../../api/account';

const Events = ({ account }) => {

  const [loading, setLoading] = React.useState(true);
  const [events, setEvents] = React.useState([]);

  React.useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      if (account) {
        const evts = await accountEvents(account.id);
        console.log("Events", evts);
        setEvents(evts);
        setLoading(false);
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
};

const mapStateToProps = state => {
  return {
    account: state.session.account,
  };
};

export default connect(mapStateToProps)(Events);
