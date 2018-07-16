import React, { Component } from 'react';
import { Row, Col, Menu } from 'antd'
import TicketManage from './TicketManage'
import SalesData from './SalesData'
import UserManage from './UserManage'
import RefundAudit from './RefundAudit'

class AdminSpace extends Component{

    state = {
        SelectedKeys:'1',
        OpenKeys:['sub1','sub2','sub3']
    };

    componentWillMount() {
        this.setState(this.props.location.state);
    };

    componentWillReceiveProps(nextProps) {
        this.setState(nextProps.location.state);
    }

    handleClick = (e) =>{
        this.setState({
            SelectedKeys:e.key
        });
    };

    render(){

        let userManage = <UserManage/>;
        let ticketManage = <TicketManage/>;
        let refundAudit = <RefundAudit/>;
        let salesData = <SalesData/>;

        let adminContent = null;

        let key = this.state.SelectedKeys;
        switch (key){
            case '1':
                adminContent = userManage;
                break;
            case '2':
                adminContent = ticketManage;
                break;
            case '3':
                adminContent = refundAudit;
                break;
            case '4':
                adminContent = salesData;
                break;
            default:
                adminContent = userManage;
        }

        let menu = <Menu
            onClick={this.handleClick}
            defaultSelectedKeys={[key]}
            selectedKeys={[this.state.SelectedKeys]}
            mode="inline"
        >
            <Menu.Item key="1">用户管理</Menu.Item>
            <Menu.Item key="2">票品管理</Menu.Item>
            <Menu.Item key="3">退款审核</Menu.Item>
            <Menu.Item key="4">销量统计</Menu.Item>
        </Menu>
        //alert
        let adminBar =
            <div style={{marginTop:20}}>
                <Row>
                    <Col span={3}/>
                    <Col span={3}>
                        <div align="center">
                            {menu}
                        </div>
                    </Col>
                    <Col span={15}>
                        <div style={{ marginLeft:20}}>
                            {adminContent}
                        </div>
                    </Col>

                </Row>
            </div>;

        return(
            adminBar
        )
    }
}

export default AdminSpace;