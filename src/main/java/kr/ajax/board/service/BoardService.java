package kr.ajax.board.service;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.ajax.board.dao.BoardDAO;
import kr.ajax.board.dto.BoardDTO;

@Service
public class BoardService {
	@Autowired BoardDAO boardDAO;
	
	Logger logger = LoggerFactory.getLogger(getClass());
	private String file_root = "C:/upload/";
	
	public List<BoardDTO> list() {
		return boardDAO.list();
	}

	private void delFile(List<String> files) {
		for (String name : files) {
			File file = new File(file_root + name);
			if(file.exists()) {
				file.delete();
			}
		}
	}
	

	public int del(List<String> delList) {
		int cnt  = 0;
		for (String idx : delList) {
			// 1. 게시글에 연결된 파일명(new_filename) 확보
			List<String > files = boardDAO.getFiles(idx);
			cnt += boardDAO.del(idx);// 2. bbs에서 해당 글 삭제
			// 3. 파일 삭제
			logger.info("files : "+files);
			delFile(files);
		}
		return cnt;
	}
}
