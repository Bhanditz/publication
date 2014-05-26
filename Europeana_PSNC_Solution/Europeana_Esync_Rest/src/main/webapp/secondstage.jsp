<html>
<body>
<h2>Hello World!</h2>

<form action="rest/synchronization1/secondStage" method="post">
<table>
  <tr>
     <td>source host ip :</td>
     <td> <input type="text" name="source_ip" value="localhost" /></td>
     </tr>
     <tr>
     <td>source  port :</td>
    <td> <input type="text" name="source_port" value="27018" /></td></tr>
    <tr>
     <td>source database name :</td>
    <td> <input type="text" name="source_databasename" value="users"/></td></tr>
    <tr>
     <td>source server username :</td>
    <td>  <input type="text" name="source_username" value=""/></td></tr>
    <tr>
     <td>source server password :</td>
     <td><input type="password" name="source_password" value="" /></td></tr>
     <tr>
     <td> destination host ip :</td>
     <td><input type="text" name="destination_ip" value="localhost"/></td></tr>
     <tr>
     <td>destination  port :</td>
     <td> <input type="text" name="destination_port" value="8983"/></td></tr>
     <tr>
     <td>destination database name :</td>
     <td><input type="text" name="destination_databasename" value=""/></td></tr>
     <tr>
     <td>destination server username :</td>
     <td><input type="text" name="destination_username" value=""/></td></tr>
     <tr>
     <td>destination serer password :</td>
     <td><input type="password" name="destination_password" value=""/></td></tr>
     <tr>
     <td>rabbitMQ host ip :</td>
     <td><input type="text" name="rabbitmq_ip" value="localhost"/></td></tr>
     <tr>
     <td>rabbitMQ port :</td>
     <td><input type="text" name="rabbitmq_port" value="5672"/></td></tr>
      <tr>
     <td>rabbitMQ username :</td>
    <td> <input type="text" name="rabbitmq_username" value="guest"/></td></tr>
     <tr>
     <td>rabbitMQ password :</td>
     <td><input type="password" name="rabbitmq_password" value="guest"/></td></tr>
     

      <tr>
     <td>source Provider :</td>
     <td><input type="text" name="source_type" value="mongoCollection"/></td></tr>
     <tr>
     <td>destination Provider :</td>
     <td><input type="text" name="destination_type" value="solrCollection"/></td></tr>
    <tr>
      
    <td colspan="2"><input type="submit" value="sysnchronize" /></td></tr>
    
    </table>
</form>
</body>
</html>



