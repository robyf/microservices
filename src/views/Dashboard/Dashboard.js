import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';

import { Loading } from '../../components';

import { Account, CreditDecision } from '../../types';
import { lendingAccount, createLendingAccount, validCreditDecision } from '../../api/account';
import { setAccount } from '../../redux/actions';

const Dashboard = ({ account, setAccount }) => {

  const [loading, setLoading] = React.useState(true);

  React.useEffect(() => {
    const fetchData = async () => {
      setLoading(true);

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

      const cd = await validCreditDecision(currentAccount.id);
      console.log("Credit Decision", cd);

      if (currentAccount.status === 'NEW' && !cd) {
        console.log('Should start scoring');
      }

      setLoading(false);
    };
    console.log('Dashboard loading');
    fetchData();
  }, []);

  if (loading) {
    return <Loading />;
  }

  return (
    <h1>Dashboard</h1>
  );
};

Dashboard.propTypes = {
  account: Account.propTypes,
  setAccount: PropTypes.func.isRequired,
};

const mapStateToProps = state => {
  return {
    account: state.session.account,
  };
};

export default connect(mapStateToProps, { setAccount })(Dashboard);
