import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';

import { Loading } from '../../components';
import { Scoring,
         NegativeCreditDecision,
         PendingCreditDecision,
         Dashboard as DashboardComponent
       } from './components';

import { Account, CreditDecision } from '../../types';
import { lendingAccount, createLendingAccount, validCreditDecision } from '../../api/account';
import { setAccount, setCreditDecision } from '../../redux/actions';

const Dashboard = ({ account, creditDecision, setAccount, setCreditDecision }) => {

  const [loading, setLoading] = React.useState(true);
  const [scoring, setScoring] = React.useState(false);
  const [negative, setNegative] = React.useState(false);
  const [pending, setPending] = React.useState(false);

  React.useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      setScoring(false);
      setNegative(false);
      setPending(false);

      if (!account) {
        let acc = await lendingAccount();
        if (!acc) {
          acc = await createLendingAccount();
        }
        setAccount(acc);
      }

      if (!creditDecision) {
        const cd = await validCreditDecision();
        setCreditDecision(cd);
      }

      setLoading(false);
    };
    console.log('Dashboard loading');
    fetchData();
  }, []);

  React.useEffect(() => {
    console.log('Account', account);
    console.log('Credit decision', creditDecision);
    setScoring(account && account.status === 'NEW' && !creditDecision);
    setNegative(account && creditDecision && !creditDecision.positive);
    setPending(account && creditDecision && creditDecision.positive && creditDecision.status === 'PENDING');
  }, [account, creditDecision]);

  if (loading) {
    return <Loading />;
  }

  if (scoring) {
    return <Scoring />;
  }

  if (negative) {
    return <NegativeCreditDecision />;
  }

  if (pending) {
    return <PendingCreditDecision />;
  }

  return (
    <DashboardComponent />
  );
};

Dashboard.propTypes = {
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

export default connect(mapStateToProps, { setAccount, setCreditDecision })(Dashboard);
