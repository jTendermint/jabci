package com.github.jtendermint.jabci.socket;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.github.jtendermint.jabci.api.ABCIAPI;
import com.github.jtendermint.jabci.types.Types.Request;
import com.github.jtendermint.jabci.types.Types.Request.ValueCase;
import com.github.jtendermint.jabci.types.Types.RequestBeginBlock;
import com.github.jtendermint.jabci.types.Types.RequestCheckTx;
import com.github.jtendermint.jabci.types.Types.RequestCommit;
import com.github.jtendermint.jabci.types.Types.RequestDeliverTx;
import com.github.jtendermint.jabci.types.Types.RequestEcho;
import com.github.jtendermint.jabci.types.Types.RequestEndBlock;
import com.github.jtendermint.jabci.types.Types.RequestFlush;
import com.github.jtendermint.jabci.types.Types.RequestInfo;
import com.github.jtendermint.jabci.types.Types.RequestInitChain;
import com.github.jtendermint.jabci.types.Types.RequestQuery;
import com.github.jtendermint.jabci.types.Types.RequestSetOption;
import com.github.jtendermint.jabci.types.Types.ResponseBeginBlock;
import com.github.jtendermint.jabci.types.Types.ResponseCheckTx;
import com.github.jtendermint.jabci.types.Types.ResponseCommit;
import com.github.jtendermint.jabci.types.Types.ResponseDeliverTx;
import com.github.jtendermint.jabci.types.Types.ResponseEcho;
import com.github.jtendermint.jabci.types.Types.ResponseEndBlock;
import com.github.jtendermint.jabci.types.Types.ResponseFlush;
import com.github.jtendermint.jabci.types.Types.ResponseInfo;
import com.github.jtendermint.jabci.types.Types.ResponseInitChain;
import com.github.jtendermint.jabci.types.Types.ResponseQuery;
import com.github.jtendermint.jabci.types.Types.ResponseSetOption;
import com.google.protobuf.GeneratedMessageV3;

/**
 * @author arnonuem
 */
@RunWith(MockitoJUnitRunner.class)
public class ASocketTest {

	@Mock Request request;
	@InjectMocks ASocket socket = new TSocket();
		
	@Test
	public void shouldConsiderAbciInterface() throws Exception {
		when( request.getValueCase() ).thenReturn( ValueCase.ECHO );
		
		socket.registerListener( new AbciListener() );
		GeneratedMessageV3 response = socket.handleRequest( request );
		
		assertTrue( response.toString().contains("ABCIAPI_WORKS") );
	}	
	
	
	private class AbciListener implements ABCIAPI {
		@Override
		public ResponseEcho requestEcho(RequestEcho req) {
			return ResponseEcho.newBuilder().setMessage("ABCIAPI_WORKS").build();
		}
		
		@Override
		public ResponseDeliverTx receivedDeliverTx(RequestDeliverTx req) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ResponseBeginBlock requestBeginBlock(RequestBeginBlock req) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ResponseCheckTx requestCheckTx(RequestCheckTx req) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ResponseCommit requestCommit(RequestCommit requestCommit) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ResponseEndBlock requestEndBlock(RequestEndBlock req) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ResponseFlush requestFlush(RequestFlush reqfl) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ResponseInfo requestInfo(RequestInfo req) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ResponseInitChain requestInitChain(RequestInitChain req) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ResponseQuery requestQuery(RequestQuery req) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ResponseSetOption requestSetOption(RequestSetOption req) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
}
