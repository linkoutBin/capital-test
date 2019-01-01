	var requestURL = "http://localhost";	
	var cookie = {
		    set:function(key,val,time){//设置cookie方法
		        var date=new Date(); //获取当前时间
		        var expiresDays=time;  //将date设置为n天以后的时间
		        date.setTime(date.getTime()+expiresDays*24*3600*1000); //格式化为cookie识别的时间
		        document.cookie=key + "=" + val +";expires="+date.toGMTString()+";path=/";  //设置cookie
		    },
		    get:function(key){//获取cookie方法
		        /*获取cookie参数*/
		        var getCookie = document.cookie.replace(/[ ]/g,"");  //获取cookie，并且将获得的cookie格式化，去掉空格字符
		        var arrCookie = getCookie.split(";")  //将获得的cookie以"分号"为标识 将cookie保存到arrCookie的数组中
		        var tips;  //声明变量tips
		        for(var i=0;i<arrCookie.length;i++){   //使用for循环查找cookie中的tips变量
		            var arr=arrCookie[i].split("=");   //将单条cookie用"等号"为标识，将单条cookie保存为arr数组
		            if(key==arr[0]){  //匹配变量名称，其中arr[0]是指的cookie名称，如果该条变量为tips则执行判断语句中的赋值操作
		                tips=arr[1];   //将cookie的值赋给变量tips
		                break;   //终止for循环遍历
		            }
		          }
		        return tips;
		        },
		    delete:function(key){ //删除cookie方法
			         var date = new Date(); //获取当前时间
			         date.setTime(date.getTime()-10000); //将date设置为过去的时间
			         document.cookie = key + "=v; expires =" +date.toGMTString()+";path=/";//设置cookie
			        }
		    
		}
	
	
	$(function(){
		$("#loginForm").submit(function(){
			var userId = $.trim($("#userid").val());
			var passwd = $.trim($("#passwd").val());
			if(''==userId||''==passwd){
        		  alert("用户名或密码为空");
        		  return;
        	}
        	var data = {
        		userId:userId,
        		password:passwd
        	};
        	$.ajax({
        		cache: false,
                contentType: "application/json;charset=utf-8",
                dataType: 'json', 
                type: "POST",
                url: requestURL+'/login/login.ajax',
                data: JSON.stringify(data),
                success: function(data) {
                     if(data.code==200) {
                    	 var userVO = data.data;
	                     window.location.href = requestURL+'/beanshop/home.htm';
                     }else{
					 	$("#result").html(data.msg);
					 	return;                  	   
                     }
                },
                complete: function(data,status) {
                	
                },
                error: function(data,status,error){
                	alert("请求失败"+status+error);
                }
        	});
        	return false;
		});
		
		
		$("#RegistForm").submit(function(){
			var userId = $.trim($("#userid").val());
			var passwd = $.trim($("#passwd").val());
			if(''==userId||''==passwd){
        		  alert("用户名或密码为空");
        	}
        	var data = {
        		userId:userId,
        		password:passwd
        	};
        	$.ajax({
        		cache: false,
                contentType: "application/json;charset=utf-8",
                dataType: 'json', 
                type: "POST",
                url: requestURL+'/login/register.ajax',
                data: JSON.stringify(data),
                success: function(data) {
                     if(data.code==200) {
                	 	if(null!=data.data){
	                        window.location.href = requestURL+'/beanshop/home.htm';
                	 	}                        
                     }else{
					 	$("#result").html(data.msg);
					 	return;                  	   
                     }
                     
                },
                complete: function(data,status) {
                	
                },
                error: function(data,status,error){
                	alert("请求失败"+status+error);
                }
        	});
        	return false;
		});
		
	});


	function register(){
		window.location.href = requestURL+'/login/register.htm';
	}

	function drawRect(){
		var c=document.getElementById("canvas");
		var ctx=c.getContext("2d");
		ctx.fillStyle="#FF0000";
		ctx.fillRect(0,0,150,75);
	}
	function drawLine(){
		var c = document.getElementById("canvas");
		var ctx = c.getContext("2d");
		ctx.moveTo(0,0);
		ctx.lineTo(200,100);
		ctx.stroke();
	}
	function drawArc(){
		var c=document.getElementById("canvas");
		var ctx=c.getContext("2d");
		ctx.beginPath();
		ctx.arc(40,40,40,0,2*Math.PI);
		ctx.stroke();
	}
	function drawWord(){
		var c=document.getElementById("canvas");
		var ctx=c.getContext("2d");
		ctx.font="30px Arial";
		ctx.fillText("Hello World",10,50);
	}
	function drawWordEmpty(){
		var c=document.getElementById("canvas");
		var ctx=c.getContext("2d");
		ctx.font="30px Arial";
		ctx.strokeText("Hello World",10,50);
	}
	function drawLineGradient(){
		var c=document.getElementById("canvas");
		var ctx=c.getContext("2d");
		 
		// 创建渐变
		var grd=ctx.createLinearGradient(0,0,200,0);
		grd.addColorStop(0,"red");
		grd.addColorStop(1,"white");
		 
		// 填充渐变
		ctx.fillStyle=grd;
		ctx.fillRect(10,10,150,80);
	}
	function drawRedialGradient(){
		var c=document.getElementById("canvas");
		var ctx=c.getContext("2d");
		 
		// 创建渐变
		var grd=ctx.createRadialGradient(100,100,5,100,100,100);
		grd.addColorStop(0,"red");
		grd.addColorStop(1,"white");
		 
		// 填充渐变
		ctx.fillStyle=grd;
		ctx.fillRect(0,0,200,200);
	}
