import { Route, Routes } from 'react-router-dom'
import GameSetup from './pages/GameSetup';
import GameBoard from './pages/GameBoard';
import Main from './pages/Main';
import AccountWindow from './components/AccountWindow';
import GameView from './pages/GamesView';

function App() {
  return (
    <div className="App">
      <AccountWindow />

      <Routes>
        <Route path='/' element={<Main />} />
        <Route path='/:gamecode' element={<Main />} />

        <Route path='/games' element={<GameView />} />

        <Route path='/games/setup/:gameid' element={<GameSetup />} />
        
        <Route path='/game/:gamecode' element={<GameBoard />} />
      </Routes>
    </div>
  );
}

export default App