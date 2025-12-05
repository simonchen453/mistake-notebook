import React from 'react';
import ReactDOM from 'react-dom/client';
import { ConfigProvider } from 'antd';
import App from './App';
// @ts-ignore
import './index.css';

ReactDOM.createRoot(document.getElementById('root')!).render(
    <React.StrictMode>
        <ConfigProvider
            theme={{
                token: {
                    colorPrimary: '#764ba2',
                    borderRadius: 8,
                    fontFamily: '"Inter", -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif',
                },
                components: {
                    Button: {
                        controlHeight: 40,
                    },
                    Card: {
                        borderRadiusLG: 16,
                    }
                }
            }}
        >
            <App />
        </ConfigProvider>
    </React.StrictMode>,
);
