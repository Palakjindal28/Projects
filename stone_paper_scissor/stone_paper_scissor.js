let userScore=0;
let compScore=0;
const userScorefinal=document.querySelector("#user");
const compScorefinal=document.querySelector("#comp");
const msg=document.querySelector("#msg");
const choices=document.querySelectorAll(".choice");
const genComp=()=>{
    let options=["rock","paper","scissor"];
    const idx=Math.floor(Math.random(options)*3);//we want number between range 0 to 2 so we will multiply by 3
    return options[idx];
}
const drawGame=()=>{
    msg.innerText="It's a draw.Please try again";
    msg.style.backgroundColor="#eaa6a8";
}

const showWinner=(userWin,userChoice,compChoice)=>{
    if(userWin){
        userScore++;
        userScorefinal.innerText=userScore;
        msg.innerText=`You Win! Your ${userChoice} beats ${compChoice}` ;
        // msg.style.marginLeft="45%";
        msg.style.backgroundColor="green";
    }else{
        compScore++;
        compScorefinal.innerText=compScore;
        msg.innerText=`You Lose! ${compChoice} beats your ${userChoice}`;
        msg.style.backgroundColor="red";
    }
}

const playGame=(userChoice)=>{
    console.log("user choice=",userChoice);
    //generate computer choice
    const compChoice=genComp();
    console.log("comp choice=",compChoice);
    if(userChoice===compChoice){
        //draw game
        drawGame();
    }
    else{
        let userWin=true;
        if(userChoice==='rock'){
            userWin=compChoice==="paper"?false:true;
        }
        else if (userChoice==='paper'){
            userWin=compChoice==="scissor"?false:true;
        }
        else{
            userWin=compChoice==="rock"?false:true;
        }
        showWinner(userWin,userChoice,compChoice);
    }
}
choices.forEach((choice)=>{
    choice.addEventListener("click",()=>{
        const userChoice=choice.getAttribute("id");
        playGame(userChoice);
    })
})