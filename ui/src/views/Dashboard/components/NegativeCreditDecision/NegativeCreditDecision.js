import React from 'react';

import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';

const NegativeCreditDecision = () => {
  return (
    <Row className="scoring-header">
      <Col className="text-center">
        We are very sorry but at this stage we cannot offer you any loan. :(
      </Col>
    </Row>
  );
};

export default NegativeCreditDecision;
