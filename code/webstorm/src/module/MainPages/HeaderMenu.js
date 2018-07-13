import React, { Component } from 'react';
import { Row, Col, Menu, Icon, Input, Avatar,BackTop } from 'antd';
import { browserHistory} from 'react-router'
import Login from './Login'
import axios from 'axios';
import "../../css/App.css"


const SubMenu = Menu.SubMenu;
const Search = Input.Search;

class HeaderMenu extends Component {

    state = {
        isLogin:false,
        isAdmin:false,
        visible:false,
        type:'',
        search:'',
        current: "home",
    };

    handleSearch = (value) => {
        axios.get("http://localhost:8080/hi")
            .then(function (response) {
                console.log(response);
                alert(response.data+" (this ajax should be deleted.)");
            })
            .catch(function (error) {
                console.log(error);
            });
        // localStorage.setItem('search',value);
        browserHistory.push({
            pathname:'/dir/all',
            state: value,
        });
        this.setState({
            current: "dir"
        });
    };
    handleLogout = () =>{
        this.setState({
            isLogin:false,
            isAdmin:false,
        });
        this.handleHomePage()
    };

    handleHomePage = () =>{
        browserHistory.push('/home');
        this.setState({
            current: "home",
        });
    };

    handleInfoSpace = (e) =>{
        localStorage.setItem('key',e.key);
        browserHistory.push({
            pathname:'/info'
        });
        this.setState({
            current: ""
        });
    };

    handleAdminSpace = (e) =>{
        localStorage.setItem('key',e.key);
        browserHistory.push({
            pathname:'/admin'
        });
        this.setState({
            current: ""
        });
    };

    handleDirectory = () =>{
        browserHistory.push('/dir/all');
        this.setState({
            current: "dir"
        });
    };

    handleAvatar = () =>{
        if(this.state.isLogin){
            localStorage.setItem('key',9);
            browserHistory.push({
                pathname:'/info'
            });
            this.setState({
                current: ""
            });
        }
        else{
            this.showModal()
        }

    };

    showModal = () => {
        this.setState({ visible: true });
    };

    handleCancel = () => {
        this.setState({ visible: false });
    };

    handleCreate = () => {
        const form = this.formRef.props.form;
        form.validateFields((err, values) => {
            if (err) {
                return;
            }
            console.log('Received values of form username: '+form.getFieldValue("username") );
            console.log('password: '+form.getFieldValue("password"));
            form.resetFields();
            if(values.username === 'admin'){
                this.setState({
                    visible: false,
                    isLogin: true,
                    isAdmin:true,
                });
                return;
            }
            this.setState({
                visible: false,
                isLogin: true,
            });
        });
    };

    saveFormRef = (formRef) => {
        this.formRef = formRef;
    };

    render() {

        let loginButton =
            <Menu mode="horizontal" style={{border:0}}>
                <Menu.Item onClick={this.showModal}>登录</Menu.Item>
                <Menu.Item>注册</Menu.Item>
            </Menu>;

        let infoButton =
            <Menu mode="horizontal" style={{border:0}}>
                <SubMenu title={<span>个人信息<Icon type="down" /></span>}>
                    <Menu.Item key="1" onClick={this.handleInfoSpace}>我的订单</Menu.Item>
                    <Menu.Item key="3" onClick={this.handleInfoSpace}>我的优惠券</Menu.Item>
                    <Menu.Item key="5" onClick={this.handleInfoSpace}>我的动态</Menu.Item>
                    <Menu.Item key="9" onClick={this.handleInfoSpace}>账号设置</Menu.Item>
                    <Menu.Item onClick={this.handleLogout}>退出登录</Menu.Item>
                </SubMenu>
            </Menu>;

        let adminButton =
            <Menu mode="horizontal" style={{border:0}}>
                <SubMenu title={<span>管理员<Icon type="down" /></span>}>
                    <Menu.Item key="1" onClick={this.handleAdminSpace}>用户管理</Menu.Item>
                    <Menu.Item key="2" onClick={this.handleAdminSpace}>票品管理</Menu.Item>
                    <Menu.Item key="3" onClick={this.handleAdminSpace}>退款审核</Menu.Item>
                    <Menu.Item key="4" onClick={this.handleAdminSpace}>销量统计</Menu.Item>
                    <Menu.Item onClick={this.handleLogout}>退出登录</Menu.Item>
                </SubMenu>
            </Menu>;

        let loginOrInfo = null;

        if(this.state.isLogin && this.state.isAdmin){
            loginOrInfo = adminButton;
        }
        else if(this.state.isLogin && !this.state.isAdmin){
            loginOrInfo = infoButton;
        }
        else{
            loginOrInfo = loginButton;
        }

        let renderHeader =
            <div>
                <Login
                    wrappedComponentRef={this.saveFormRef}
                    visible={this.state.visible}
                    onCancel={this.handleCancel}
                    onLogin={this.handleCreate}
                />
                <Menu mode="horizontal">
                    <Row>
                        <Col span={3} onClick={this.handleHomePage}>
                            <div align="center"><Icon type="global"/>聚票网</div>
                        </Col>
                        <Col span={7}>
                            <Menu
                                mode="horizontal"
                                style={{border: 0}}
                                selectedKeys={[this.state.current]}
                                defaultSelectedKeys="home"
                            >
                                <Menu.Item key="home" onClick={this.handleHomePage}>首页</Menu.Item>
                                <Menu.Item key="dir" onClick={this.handleDirectory}>全部分类</Menu.Item>
                            </Menu>
                        </Col>
                        <Col span={7}>
                            <Search
                                placeholder="搜索   TODO: 分享  购物车  销量  回复  退款申请  找回密码"
                                onSearch={value => this.handleSearch(value)}
                                defaultValue={this.state.search}
                                enterButton
                            />
                        </Col>
                        <Col span={3}/>
                        <Col span={1}>
                            <Avatar icon="user" onClick={this.handleAvatar}/>
                        </Col>
                        <Col span={3}>
                            {loginOrInfo}
                        </Col>
                    </Row>
                </Menu>
                <BackTop/>
            </div>;
        return (
            renderHeader
        )
    }
}

export default HeaderMenu;
