import React from 'react';
import {BrowserRouter as Router, Navigate, Route, Routes} from 'react-router-dom';
import {UserProvider} from './context/UserContext';
import Header from './components/Header';
import Footer from './components/Footer';
import Menu from './components/Menu';
import LoginPage from './pages/LoginPage';
import Dashboard from './pages/Dashboard';
import EvaluationsAdmin from './pages/EvaluationsAdmin';
import EvaluationsTeacher from './pages/EvaluationsTeacher';
import EvaluationsStudent from './pages/EvaluationsStudent';
import SubmitEvaluation from './pages/SubmitEvaluation';
import Users from './pages/Users';
import {RedirectIfAuthenticated, RequireAuth} from './routes/PrivateRoute.tsx';
import UnauthorizedPage from "./pages/Unauthorized.tsx";
import "./styles/App.css";

const App: React.FC = () => {
    return (
        <UserProvider>
            <Router>
                <Header />
                <Menu />
                <Routes>
                    {/* Redirect to /login on app startup */}
                    <Route path="/" element={<Navigate to="/login" replace />} />

                    {/* Public Route: Login */}
                    <Route element={<RedirectIfAuthenticated />}>
                        <Route path="/login" element={<LoginPage />} />
                    </Route>

                    {/* Protected Routes */}
                    <Route element={<RequireAuth />}>
                        <Route path="/dashboard" element={<Dashboard />} />
                    </Route>
                    <Route element={<RequireAuth allowedRoles={['ADMIN']} />}>
                        <Route path="/users" element={<Users />} />
                        <Route path="/evaluations" element={<EvaluationsAdmin />} />
                    </Route>
                    <Route element={<RequireAuth allowedRoles={['TEACHER']} />}>
                        <Route path="/evaluations-teacher" element={<EvaluationsTeacher />} />
                    </Route>
                    <Route element={<RequireAuth allowedRoles={['STUDENT']} />}>
                        <Route path="/evaluations-student" element={<EvaluationsStudent />} />
                        <Route path="/submit-evaluation" element={<SubmitEvaluation />} />
                    </Route>

                    <Route path="/unauthorized" element={<UnauthorizedPage />} />

                    {/* Catch-all */}
                    <Route path="*" element={<Navigate to="/login" replace />} />
                </Routes>
                <Footer />
            </Router>
        </UserProvider>
    );
};

export default App;
