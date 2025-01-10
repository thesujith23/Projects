import React,{useState} from "react";

export const TodoForm=({addTodo})=>{
    const [value,setValue]=useState("")
    const handleSubmit=(e)=>{
        e.preventDefault();
        addTodo(value);
        setValue("")
    }
    return(
        <form className='TodoForm' onSubmit={handleSubmit}>
            <input type="text" className="todo-input" placeholder="what is the task?" value={value} onChange={(e)=>setValue(e.target.value)}/>
            <button className="todo-btn" type="submit" disabled={!value.trim()}>Add Task</button>
        </form>
    );
};