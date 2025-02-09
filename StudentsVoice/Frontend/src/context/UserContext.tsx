import React, {createContext, useContext, useEffect, useState} from 'react';
import {getCurrentUser} from '../api/AuthService.ts';
import {User} from '../types/User.ts';

interface UserContextType {
    user: User | null;
}

const UserContext = createContext<UserContextType | undefined>(undefined);

export const UserProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    const [user, setUser] = useState<User | null>(null);

    useEffect(() => {
        const fetchUser = async () => {
            const currentUser = await getCurrentUser(); // Await the promise
            setUser(currentUser);
            console.log('User fetched:', currentUser); // Log the updated user
        };
        fetchUser().then();
    }, []);

    return <UserContext.Provider value={{ user }}>{children}</UserContext.Provider>;
};

export const useUser = () => {
    const context = useContext(UserContext);
    if (!context) {
        throw new Error('useUser must be used within a UserProvider');
    }
    return context;
};
