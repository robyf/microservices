import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';

import { Loading } from '../../components';
import { Scoring, NegativeCreditDecision } from './components';

import { Account, CreditDecision } from '../../types';
import { lendingAccount, createLendingAccount, validCreditDecision } from '../../api/account';
import { setAccount, setCreditDecision } from '../../redux/actions';

const Dashboard = ({ account, creditDecision, setAccount, setCreditDecision }) => {

  const [loading, setLoading] = React.useState(true);
  const [scoring, setScoring] = React.useState(false);
  const [negative, setNegative] = React.useState(false);

  React.useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      setScoring(false);
      setNegative(false);

      let currentAccount;
      if (account) {
        console.log('Account from state', account);
        currentAccount = account;
      } else {
        let acc = await lendingAccount();
        console.log('Lending account', acc);
        if (!acc) {
          acc = createLendingAccount();
          console.log('Create lending account', acc);
        }
        currentAccount = acc;
        setAccount(acc);
      }

      if (!creditDecision) {
        const cd = await validCreditDecision(currentAccount.id);
        console.log("Credit Decision", cd);
        setCreditDecision(cd);
      }

      setLoading(false);
    };
    console.log('Dashboard loading');
    fetchData();
  }, []);

  React.useEffect(() => {
    setScoring(account && account.status === 'NEW' && !creditDecision);
    setNegative(account && creditDecision && !creditDecision.positive);
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

  return (
    <h1>Dashboard</h1>
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
