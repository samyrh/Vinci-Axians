import React, { useState, useEffect } from 'react';
import '../styles/style.css';

export default function Index() {
    const [bubblesPaused, setBubblesPaused] = useState(false);
    const [rotationEnabled, setRotationEnabled] = useState(true);
    const [rotationAngle, setRotationAngle] = useState(0);
    const [mousePosition, setMousePosition] = useState({ x: 0 });

    const toggleBubbles = () => {
        setBubblesPaused(!bubblesPaused);
    };

    const toggleRotation = () => {
        setRotationEnabled(!rotationEnabled);
    };

    useEffect(() => {
        const handleMouseMove = (event: MouseEvent) => {
            if (rotationEnabled) {
                const x = (event.clientX / window.innerWidth) - 0.5;
                const newAngle = x * 360;
                setRotationAngle(newAngle);
                setMousePosition({ x });
            }
        };

        window.addEventListener('mousemove', handleMouseMove);

        return () => {
            window.removeEventListener('mousemove', handleMouseMove);
        };
    }, [rotationEnabled]);

    return (
        <div className="page-container">
            <div className={`bubbles ${bubblesPaused ? 'paused' : ''}`}>
                {Array.from({ length: 20 }).map((_, index) => (
                    <div
                        key={index}
                        className={`bubble animal-bubble`} // Added class for animal shapes
                        style={{
                            transform: `translate3d(${mousePosition.x * 100}px, 0, 0)`,
                        }}
                    />
                ))}
            </div>
            <div
                className="content-container"
                style={{
                    transform: `rotateY(${rotationAngle}deg)`,
                    transition: 'transform 0.1s',
                }}
            >
                <h1 className="main-title">Vinci Energies AI Chat Assistant</h1>
                <div className="ai-description">
                    <p className="description-text">
                        Powered by cutting-edge AI technology, Vinci Energies Chat AI utilizes Llama 3, RAG (Retrieval-Augmented Generation),
                        and chunk-based intelligent processing to answer any question with contextual precision and speed.
                    </p>
                    <p className="description-text">
                        This AI can tackle your most complex queries, providing human-like responses and extracting real-time data across various domains.
                        Seamless interaction with intelligent energy solutions.
                    </p>
                </div>
                <div className="button-container">
                    <button className="toggle-button" onClick={toggleBubbles}>
                        {bubblesPaused ? 'Play Bubbles' : 'Pause Bubbles'}
                    </button>
                    <button className="toggle-button" onClick={toggleRotation}>
                        {rotationEnabled ? 'Disable 360° Rotation' : 'Enable 360° Rotation'}
                    </button>
                </div>
            </div>
        </div>
    );
}
