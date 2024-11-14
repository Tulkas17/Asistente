const express = require('express');
const path =  require('path');
const app = express();

//Files directory 
app.use(express.static(path.join(__dirname, 'public')));

//Route html 
app.get('/', (req, res) =>{
    res.sendFile(path.join(__dirname, '', "./main-view.html"));
});

//Start server
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
    console.log("Server is running on http://localhost:3000");
})