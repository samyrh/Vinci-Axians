import { NavLink, Outlet } from "react-router-dom";
import '../styles/stylenav.css';

export default function Layout() {
    return (
        <div className="container">
            <nav>
                <h1>Admin Chat</h1>
                <div>
                    <NavLink to="/">Home</NavLink>
                    <NavLink to="/chat">Chat</NavLink>
                </div>
            </nav>
            <main>
                <Outlet />
            </main>
        </div>
    );
}
